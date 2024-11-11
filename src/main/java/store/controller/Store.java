package store.controller;

import store.view.InputView;
import store.view.OutputView;

public class Store {
    private final InputView inputView;
    private final OutputView outputView;

    public Store() {
        this.inputView = new InputView();
        this.outputView = new OutputView();
    }

    public void run() {

    }
}
