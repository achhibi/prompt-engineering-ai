package net.amch.labs.promptengineeringai.usecases.model;

import java.util.List;

public record SpringVersion(String version, Integer year, List<String> features, List<String> contributors) {
}
