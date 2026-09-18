#version 150

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform float GameTime;
uniform float Width;
uniform float Height;
uniform float MarginX;
uniform float MarginY;
uniform float Speed;
uniform float TimeOffset;
uniform float Intensity;
uniform float XFrequency;
uniform float YFrequency;

in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    float time = GameTime * Speed + TimeOffset;
    vec2 expandedSize = vec2(Width + MarginX * 2.0, Height + MarginY * 2.0);
    vec2 targetPosition = texCoord0 * expandedSize - vec2(MarginX, MarginY);
    if (targetPosition.x < 0.0 || targetPosition.x >= Width
            || targetPosition.y < 0.0 || targetPosition.y >= Height) {
        discard;
    }

    vec2 uv = targetPosition / vec2(Width, Height);
    vec2 distortion = vec2(
            cos(uv.y * XFrequency + time),
            sin(uv.x * YFrequency + time)
    ) / max(Intensity, 1.0) * vec2(Width, Height);
    vec2 sourcePosition = clamp(
            targetPosition - distortion,
            vec2(0.0),
            vec2(Width, Height)
    );
    vec4 textureColor = texture(Sampler0, sourcePosition / vec2(Width, Height));
    if (textureColor.a == 0.0) {
        discard;
    }
    fragColor = textureColor * vertexColor * ColorModulator;
}
