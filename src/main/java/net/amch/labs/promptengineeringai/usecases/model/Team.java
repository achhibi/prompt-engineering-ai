package net.amch.labs.promptengineeringai.usecases.model;

import java.util.List;

public record Team(String name, List<String> players, String coach, String country, Integer year) {
}