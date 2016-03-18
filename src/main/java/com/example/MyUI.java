package com.example;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.servlet.annotation.WebServlet;
import java.util.List;

@Theme("valo")
@Push
public class MyUI extends UI {

    private BackendService backendService = new BackendService();

    private BeanItemContainer<Entity> container = new BeanItemContainer<>(Entity.class);

    private ProgressBar progressBar = new ProgressBar();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Button loadSync = new Button("Load synchronously", FontAwesome.ARROW_RIGHT);
        loadSync.addStyleName(ValoTheme.BUTTON_DANGER);
        loadSync.setWidth("220px");
        loadSync.addClickListener(this::loadSyncClicked);

        Button loadAsync = new Button("Load asynchronously", FontAwesome.SPINNER);
        loadAsync.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        loadAsync.setWidth("220px");
        loadAsync.addClickListener(this::loadAsyncClicked);

        Button testButton = new Button("Click this while loading", FontAwesome.MOUSE_POINTER);
        testButton.setWidth("220px");
        testButton.addClickListener(e -> Notification.show("Test notification"));

        progressBar.setCaption("Loading...");
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);

        Grid grid = new Grid(container);
        grid.setSizeFull();

        VerticalLayout layout = new VerticalLayout(loadSync, loadAsync, testButton, progressBar, grid);
        layout.setExpandRatio(grid, 1);
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);
    }

    private void loadSyncClicked(Button.ClickEvent event) {
        // do all the remove, load data, update UI in the same thread:
        container.removeAllItems();
        List<Entity> entities = backendService.findAllEntities();
        container.addAll(entities);
    }

    private void loadAsyncClicked(Button.ClickEvent event) {
        container.removeAllItems();
        progressBar.setVisible(true); // give the user some visual hint about loading taking place

        // perform the data load in a separate thread
        loadDataInNewThread();
    }

    private void loadDataInNewThread() {
        new Thread(() -> {
            List<Entity> entities = backendService.findAllEntities();

            // this is needed because we are modifying the UI from a different thread:
            UI.getCurrent().access(() -> {
                progressBar.setVisible(false);
                container.addAll(entities);
                progressBar.setVisible(false);
            });
        }).start();
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

}
