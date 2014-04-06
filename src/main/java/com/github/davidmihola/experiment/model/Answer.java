package com.github.davidmihola.experiment.model;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

import com.github.davidmihola.experiment.ExperimentActivity;

/**
 * Created by david on 05.03.14.
 */
public final class Answer {

    private final Size size;
    private final Color color;
    private final Shape shape;
    private final Thing thing;

    public Answer (Size size, Color color, Shape shape, Thing thing) {
        this.size = size;
        this.color = color;
        this.shape = shape;
        this.thing = thing;
    }

    public enum Size {
        BIG,
        SMALL
    }

    public enum Color {
        RED,
        BLUE
    }

    public enum Shape {
        ROUND,
        SQUARE
    }

    public enum Thing {
        HOUSE,
        FACE
    }
    public String toString() {
        return size.toString() + " " + color.toString() + " " + shape.toString() + " " + thing.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        if (color != answer.color) return false;
        if (shape != answer.shape) return false;
        if (size != answer.size) return false;
        if (thing != answer.thing) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = size != null ? size.hashCode() : 0;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (shape != null ? shape.hashCode() : 0);
        result = 31 * result + (thing != null ? thing.hashCode() : 0);
        return result;
    }

    public static class AnswerListBuilder {

        Set<Size>  allowedSizes  = new HashSet<Size>();
        Set<Color> allowedColors = new HashSet<Color>();
        Set<Shape> allowedShapes = new HashSet<Shape>();
        Set<Thing> allowedThings = new HashSet<Thing>();

        public AnswerListBuilder withSize(Answer.Size size) {
            allowedSizes.add(size);
            return this;
        }

        public AnswerListBuilder or(Answer.Size size) {
            allowedSizes.add(size);
            return this;
        }

        public AnswerListBuilder withColor(Answer.Color color) {
            allowedColors.add(color);
            return this;
        }

        public AnswerListBuilder or(Answer.Color color) {
            allowedColors.add(color);
            return this;
        }

        public AnswerListBuilder withShape(Answer.Shape shape) {
            allowedShapes.add(shape);
            return this;
        }

        public AnswerListBuilder or(Answer.Shape shape) {
            allowedShapes.add(shape);
            return this;
        }

        public AnswerListBuilder withThing(Answer.Thing thing) {
            allowedThings.add(thing);
            return this;
        }

        public AnswerListBuilder or(Answer.Thing thing) {
            allowedThings.add(thing);
            return this;
        }

        public ImmutableList<Answer> now() {
            ImmutableList.Builder<Answer> builder = ImmutableList.<Answer>builder();

            if (allowedSizes.isEmpty()) {
                allowedSizes.addAll(Arrays.asList(Size.values()));
            }

            if (allowedColors.isEmpty()) {
                allowedColors.addAll(Arrays.asList(Color.values()));
            }

            if (allowedShapes.isEmpty()) {
                allowedShapes.addAll(Arrays.asList(Shape.values()));
            }

            if (allowedThings.isEmpty()) {
                allowedThings.addAll(Arrays.asList(Thing.values()));
            }

            for (Size size : allowedSizes) {
                for (Color color : allowedColors) {
                    for (Shape shape : allowedShapes) {
                        for (Thing thing : allowedThings) {
                            builder.add(new Answer(size, color, shape, thing));
                        }
                    }
                }
            }

            return builder.build();
        }
    }

    public static AnswerListBuilder getAnswers() {
        return new AnswerListBuilder();
    }
}
