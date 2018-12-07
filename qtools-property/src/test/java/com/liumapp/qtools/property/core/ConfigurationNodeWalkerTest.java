package com.liumapp.qtools.property.core;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.liumapp.qtools.property.core.commented.CommentedConfigurationNode;
import com.liumapp.qtools.property.core.commented.SimpleCommentedConfigurationNode;
import com.liumapp.qtools.property.core.util.ConfigurationNodeWalker;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * file ConfigurationNodeWalkerTest.java
 * author liumapp
 * github https://github.com/liumapp
 * email liumapp.com@gmail.com
 * homepage http://www.liumapp.com
 * date 2018/12/7
 */
public class ConfigurationNodeWalkerTest {

    private static final Function<ConfigurationNodeWalker.VisitedNode, String> PATH_TO_STRING = visitedNode -> {
        return StreamSupport.stream(visitedNode.getPath().spliterator(), false)
                .map(o -> {
                    if (o == null) {
                        return "root";
                    }
                    return o.toString();
                })
                .collect(Collectors.joining(" "));
    };

    private static final List<String> EXPECTED_BREADTH_FIRST_ORDER = ImmutableList.of(
            "root",
            "l1-1",
            "l1-2",
            "l1-1 l2-1",
            "l1-1 l2-2",
            "l1-1 l2-3",
            "l1-1 l2-4",
            "l1-2 l2-1",
            "l1-1 l2-3 l3-1",
            "l1-1 l2-3 l3-2",
            "l1-2 l2-1 0",
            "l1-2 l2-1 1",
            "l1-2 l2-1 2"
    );

    private static final List<String> EXPECTED_DEPTH_FIRST_PRE_ORDER = ImmutableList.of(
            "root",
            "l1-1",
            "l1-1 l2-1",
            "l1-1 l2-2",
            "l1-1 l2-3",
            "l1-1 l2-3 l3-1",
            "l1-1 l2-3 l3-2",
            "l1-1 l2-4",
            "l1-2",
            "l1-2 l2-1",
            "l1-2 l2-1 0",
            "l1-2 l2-1 1",
            "l1-2 l2-1 2"
    );

    private static final List<String> EXPECTED_DEPTH_FIRST_POST_ORDER = ImmutableList.of(
            "l1-1 l2-1",
            "l1-1 l2-2",
            "l1-1 l2-3 l3-1",
            "l1-1 l2-3 l3-2",
            "l1-1 l2-3",
            "l1-1 l2-4",
            "l1-1",
            "l1-2 l2-1 0",
            "l1-2 l2-1 1",
            "l1-2 l2-1 2",
            "l1-2 l2-1",
            "l1-2",
            "root"
    );

    @Test
    public void testWalker() {
        CommentedConfigurationNode node = SimpleCommentedConfigurationNode.root();

        node.getNode("l1-1").setValue(1);
        node.getNode("l1-1", "l2-1").setValue(1);
        node.getNode("l1-1", "l2-2").setValue(1);
        node.getNode("l1-1", "l2-3", "l3-1").setValue(1);
        node.getNode("l1-1", "l2-3", "l3-2").setValue(1);
        node.getNode("l1-1", "l2-4").setValue(1);
        node.getNode("l1-2").setValue(1);
        node.getNode("l1-2", "l2-1").setValue(ImmutableList.of(1, 2, 3));

        List<ConfigurationNodeWalker.VisitedNode<CommentedConfigurationNode>> breadthFirst = new ArrayList<>();
        Iterators.addAll(breadthFirst, ConfigurationNodeWalker.BREADTH_FIRST.walkWithPath(node));

        List<ConfigurationNodeWalker.VisitedNode<CommentedConfigurationNode>> depthFirstPre = new ArrayList<>();
        Iterators.addAll(depthFirstPre, ConfigurationNodeWalker.DEPTH_FIRST_PRE_ORDER.walkWithPath(node));

        List<ConfigurationNodeWalker.VisitedNode<CommentedConfigurationNode>> depthFirstPost = new ArrayList<>();
        Iterators.addAll(depthFirstPost, ConfigurationNodeWalker.DEPTH_FIRST_POST_ORDER.walkWithPath(node));

        Assert.assertEquals(13, breadthFirst.size());
        Assert.assertEquals(13, depthFirstPre.size());
        Assert.assertEquals(13, depthFirstPost.size());

        List<String> breadthFirstOrder = breadthFirst.stream().map(PATH_TO_STRING).collect(Collectors.toList());
        Assert.assertEquals(EXPECTED_BREADTH_FIRST_ORDER, breadthFirstOrder);

        List<String> depthFirstPreOrder = depthFirstPre.stream().map(PATH_TO_STRING).collect(Collectors.toList());
        Assert.assertEquals(EXPECTED_DEPTH_FIRST_PRE_ORDER, depthFirstPreOrder);

        List<String> depthFirstPostOrder = depthFirstPost.stream().map(PATH_TO_STRING).collect(Collectors.toList());
        Assert.assertEquals(EXPECTED_DEPTH_FIRST_POST_ORDER, depthFirstPostOrder);
    }

}
