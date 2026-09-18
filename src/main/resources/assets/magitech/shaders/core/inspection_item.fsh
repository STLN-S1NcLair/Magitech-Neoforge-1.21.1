#version 150

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform float Alpha;

in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    if (color.a < 0.1) {
        discard;
    }
    color.a *= Alpha;
    fragColor = color;
}
