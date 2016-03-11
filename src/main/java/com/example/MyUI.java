package com.example;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;

@Theme("valo")
@Push
public class MyUI extends UI {

    private BackendService backendService = new BackendService();

    private BeanItemContainer<Entity> container = new BeanItemContainer<>(Entity.class);

    private ProgressBar progressBar = new ProgressBar();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Button loadSync = new Button("Load synchronously", FontAwesome.ARROW_RIGHT);
        loadSync.addClickListener(this::loadSyncClicked);

        Button loadAsync = new Button("Load asynchronously", FontAwesome.REFRESH);
        loadAsync.addClickListener(this::loadAsyncClicked);

        progressBar.setCaption("Loading...");
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);

        Grid grid = new Grid(container);
        grid.setSizeFull();

        VerticalLayout layout = new VerticalLayout(loadSync, loadAsync, progressBar, grid);
        layout.setExpandRatio(grid, 1);
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);
    }

    private void loadSyncClicked(Button.ClickEvent event) {
        loadData();
    }

    private void loadAsyncClicked(Button.ClickEvent event) {
        new Thread(this::loadData).start(); // start the long process in a separate thread
        progressBar.setVisible(true); // give the user some visual hint about it
    }

    private void loadData() {
        container.removeAllItems();
        container.addAll(backendService.findAllEntities());

        // modify the UI from a separate thread, possibly
        UI.getCurrent().access(this::dataLoaded);
    }

    private void dataLoaded() {
        Notification.show("Data loaded.");
        progressBar.setVisible(false);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

}
