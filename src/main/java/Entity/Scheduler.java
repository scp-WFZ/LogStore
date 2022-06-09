package Entity;

import java.util.*;

public class Scheduler {
    static int n;
    static Node s, t;
    static int[] dep;
    static Map<Integer, Node> nodeMap;
    static int[] cur;
    /**
     * 使用 monitor里面的属性，直接修改属性里面的值包括router table,添加到 worker或者shard
     * @param monitor the Monitor class
     */
    static void updateRouter(Monitor monitor) {
        n = monitor.tenants.size() + monitor.shards.size() + 2;
        dep = new int[n + 2];
        nodeMap = new HashMap<>();
        cur = new int[n + 2];
        for (int i = 0; i < n + 2; i++) {
            new Node(i);
        }
        s = nodeMap.get(0); t = new Node(n + 1);
        double work = 0;
        for (Tenant tenant: monitor.tenants) {
            work += tenant.cur_wait_to_process;
        }
        s.add(s.nodeId, n - 1, monitor.tenants.get(monitor.tenants.size() - 1).cur_wait_to_process);
        for (int i = 0; i < monitor.tenants.size(); i++) {
            Tenant tenant = monitor.tenants.get(i);
            s.add(n - 1, i + 1, tenant.task_size_max);
            for (int j = 0; j < monitor.shards.size(); j++) {
                // Shard shard = monitor.shards.get(j);
                s.add(i + 1, j + monitor.tenants.size() + 1, tenant.task_size_max);
            }
        }
        for (int j = 0; j < monitor.shards.size(); j++) {
            Shard shard = monitor.shards.get(j);
            s.add(j + monitor.tenants.size() + 1, t.nodeId, shard.max_size - shard.cur_processing_size);
        }
        Map<Integer, Double> result = maxFlow(monitor);
        List<Monitor.Factor> list = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry: result.entrySet()) {
            list.add(new Monitor.Factor(monitor.shards.get(entry.getKey()).id, entry.getValue() / monitor.shards.get(entry.getKey()).max_size));
        }
        monitor.routerTable.put(monitor.tenants.get(monitor.tenants.size() - 1).id, list);
    }

    static void updateRouter0(Monitor monitor) {
        double work = monitor.tenants.get(monitor.tenants.size() - 1).cur_wait_to_process;
        List<Monitor.Factor> list = new ArrayList<>();
        while (work > 0.001) {
            double maxn = 0;
            int id = 0;
            for (int i = 0; i < monitor.shards.size(); i++) {
                Shard shard = monitor.shards.get(i);
                if (maxn < shard.max_size - shard.cur_processing_size) {
                    maxn = shard.max_size - shard.cur_processing_size;
                    id = i;
                }
            }
            if (maxn > 0.001) {
                list.add(new Monitor.Factor(monitor.shards.get(id).id, maxn / monitor.shards.get(id).max_size));
            } else {
                break;
            }
        }
        monitor.routerTable.put(monitor.tenants.get(monitor.tenants.size() - 1).id, list);
    }

    public static boolean spfa() {
        boolean[] flag = new boolean[n + 2];
        for (int i = 0; i < n + 2; i++) {
            flag[i] = false;
        }
        Queue<Node> q = new LinkedList<>();
        q.add(s);
        dep[s.nodeId] = 0;
        while (!q.isEmpty()) {
            Node node = q.poll();
            for (int i = 0; i < node.edges.size(); i++) {
                Edge edge = node.edges.get(i);
                if (!flag[edge.to] && edge.flow < edge.cap) {
                    dep[edge.to] = dep[node.nodeId] + 1;
                    flag[edge.to] = true;
                    q.add(nodeMap.get(edge.to));
                }
            }
        }
        return flag[t.nodeId];
    }

    public static Map<Integer, Double> dfs(int pos, double demand) {
        Map<Integer, Double> result = new HashMap<>();
        if (pos == t.nodeId || demand <= 0) {
            return result;
        }
        double r;
        Node node = nodeMap.get(pos);
        for (; cur[pos] < node.edges.size(); cur[pos]++) {
            Edge edge = node.edges.get(cur[pos]);
            Edge back = edge.back;
            Node to = nodeMap.get(edge.to);
            if (dep[to.nodeId] == dep[pos] + 1) {
                Map<Integer, Double> temp = dfs(to.nodeId, Math.min(demand, edge.cap - edge.flow));
                r = 0;
                for (Map.Entry<Integer, Double> entry: temp.entrySet()) {
                    r += entry.getValue();
                }
                edge.flow += r;
                back.flow -= r;
                if (!result.containsKey(pos)) {
                    result.put(pos, r);
                } else {
                    double flow = result.get(pos);
                    result.remove(pos);
                    result.put(pos, flow + r);
                }
                demand -= r;
                if (demand < 0.0001) {
                    break;
                }
            }
        }
        return result;
    }

    public static Map<Integer, Double> maxFlow(Monitor monitor) {
        Map<Integer, Double> result = new HashMap<>();
        while (spfa()) {
            for (int i = 0; i < n + 2; i++) {
                cur[i] = 0;
            }
            Map<Integer, Double> ans = dfs(cur[s.nodeId], 1);
            if (ans.size() == 0) {
                return  result;
            }
            double total = 0;
            for (Map.Entry<Integer, Double> entry: ans.entrySet()) {
                if (entry.getKey() < 1 + monitor.tenants.size() || entry.getValue() < 0) {
                    continue;
                }
                if (result.containsKey(entry.getKey())) {
                    double value = result.get(entry.getKey());
                    result.put(entry.getKey(), value + entry.getValue());
                } else {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
            for (Map.Entry<Integer, Double> entry: result.entrySet()) {
                total += entry.getValue();
                if (Math.abs(total - monitor.tenants.get(monitor.tenants.size() - 1).cur_wait_to_process) < 0.001) {
                    return result;
                }
            }
        }
        return result;
    }

    private static class Edge {
         public final int from, to;
         public final double cap;
         public double flow;
         public Edge back;

         public Edge(int from, int to, double cap, double flow) {
             this.from = from;
             this.to = to;
             this.cap = cap;
             this.flow = flow;
         }
    }

    private static class Node {
         public int nodeId;
         public ArrayList<Edge> edges;

         public Node(int nodeId) {
             this.nodeId = nodeId;
             this.edges = new ArrayList<>();
             nodeMap.put(this.nodeId, this);
         }

         public void add(int from, int to, double cap) {
             Edge edgeFrom = new Edge(nodeMap.get(from).nodeId, nodeMap.get(to).nodeId, cap, 0);
             Edge edgeTo = new Edge(nodeMap.get(to).nodeId, nodeMap.get(from).nodeId, 0, 0);
             edgeFrom.back = edgeTo;
             edgeTo.back = edgeFrom;
             nodeMap.get(from).edges.add(edgeFrom);
             nodeMap.get(to).edges.add(edgeTo);
         }
    }
}
