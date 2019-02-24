package org.laeq.model;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;

public class Icon extends Group {
    protected double size;
    protected final String path;
    protected final String color;
    protected final SVGPath svg;
    protected final float svgRatio;

    public Icon(Category category, double size)  {
        this.path = category.getIcon();
        this.size = size;
        this.color = category.getColor();
        this.svg = new SVGPath();
        this.svgRatio = 0.50f;
        svg.setContent(path);
        svg.setLayoutX(getX());
        svg.setLayoutY(getY());
        svg.setScaleX(getScale());
        svg.setScaleY(getScale());

        getChildren().addAll(getCanvas(), svg);
    }

    public Icon(String path, String color){
        this.path = path;
        this.size = 1;
        this.color = color;
        this.svgRatio = 0.5f;

        svg = new SVGPath();
        svg.setContent(path);
        svg.setSmooth(true);
        svg.setFill(Paint.valueOf(color));
        svg.setScaleX(1);
        svg.setScaleY(1);

        getChildren().add(svg);
    }

    public Icon(String path, String color, int size){
        this.path = path;
        this.size = size;
        this.color = color;
        this.svgRatio = 0.7f;

        svg = new SVGPath();
        svg.setContent(path);
        svg.setSmooth(true);
        svg.setFill(Paint.valueOf(color));
        svg.setScaleX(getScale());
        svg.setScaleY(getScale());

        getChildren().add(svg);
    }

    private Canvas getCanvas(){
        Canvas canvas = new Canvas(size, size);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.valueOf(org.laeq.graphic.Color.light));
        gc.fillOval(0,0, size, size);
        canvas.setOpacity(1);

        return canvas;
    }

    private double getScale(){
        return svgRatio / (svg.getLayoutBounds().getHeight() / size);
    }

    private double getX(){
        return size / 2 - svg.getLayoutBounds().getWidth() / 2;
    }

    public double getY(){
        return size / 2 - svg.getLayoutBounds().getHeight() / 2;
    }

    public void setPath(String s) {
        this.svg.setContent(s);
    }

    public void setSize(double s){
        this.size = s;
    }

    public void setContent(String content) {
        this.svg.setContent(content);
    }
}