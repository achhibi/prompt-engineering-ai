package net.amch.labs.promptengineeringai.usecases.model;

import java.util.List;

public record Histo(List<Conso> consos, List<Forcast> forcasts) {

    public record Conso(String date, float valeurConso) {
    }
    public record Forcast(String date, float valeurForcast) {
    }
}