package net.stln.magitech.feature.tool.material;

import net.stln.magitech.feature.tool.part.ToolPart;

import java.util.Map;

public record SynthesisedToolMaterial(Map<ToolPart, ToolMaterial> materials) {

}
