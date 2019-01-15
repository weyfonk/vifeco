package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;

import java.util.Collections;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class SubMenuView extends AbstractJavaFXGriffonView {
    private SubMenuController controller;
    private SubMenuModel model;

    @MVCMember @Nonnull
    private VifecoView parentView;

    @FXML
    private Label clickLabel;

    @MVCMember
    public void setController(@Nonnull SubMenuController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull SubMenuModel model) {
        this.model = model;
    }

    @Override
    public void initUI() {
        Node node = loadFromFXML();



        connectActions(node, controller);
    }

}
