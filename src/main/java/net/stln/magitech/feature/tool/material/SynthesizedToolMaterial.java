package net.stln.magitech.feature.tool.material;

import net.stln.magitech.feature.tool.part.ToolPart;

import java.util.Map;

public record SynthesizedToolMaterial(Map<ToolPart, ToolMaterial> materials) {

}
