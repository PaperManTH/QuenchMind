package com.cuizhi.agent.config;

import org.springframework.context.annotation.Configuration;

/**
 * @Author thpaperman
 * @Description TODO
 * @Date 2026/4/23
 * @Version 1.0
 */
@Configuration
public class GraphConfig {

    /*@Bean
    public CompiledGraph quickStartGraph() throws GraphStateException {
        KeyStrategyFactory factory = () -> Map.of("input1", new ReplaceStrategy(),
                "input2", new ReplaceStrategy());
        // 定义状态图
        StateGraph stateGraph = new StateGraph("quickStartGraph", factory);

        // 定义节点
        stateGraph.addNode("start", AsyncNodeAction.node_async(new NodeAction() {
            @Override
            public Map<String, Object> apply(OverAllState state) {
                return Map.of("input1", "input1",
                        "input2", "input2");
            }
        }));

        // 定义边
        stateGraph.addEdge(StateGraph.START, "node1");
        stateGraph.addEdge("node2", StateGraph.END);

        // 编译状态图
        return stateGraph.compile();
    }*/
}
