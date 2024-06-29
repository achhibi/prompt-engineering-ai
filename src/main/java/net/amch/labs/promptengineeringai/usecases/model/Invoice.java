package net.amch.labs.promptengineeringai.usecases.model;

import java.util.List;

public record Invoice(List<Article> articleList, float totalAmount) {
    public record Article(String label, float price, int quantity) {
    }
}
