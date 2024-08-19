package com.frontend.ui

/**
 *
 * @author FizzPu
 * @since 2024/4/17 20:12
 * */
class FlameGraphImage implements UiComponent {
    private final String images;

    FlameGraphImage(String images) {
        this.images = images
    }

    @Override
    String getHtml() {
        return images;
    }
}
