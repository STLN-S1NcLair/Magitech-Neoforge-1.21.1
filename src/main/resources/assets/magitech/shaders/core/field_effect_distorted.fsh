#version 150

#moj_import <lodestone:common_math.glsl>

uniform sampler2D Sampler0;
uniform float LumiTransparency;
uniform vec4 ColorModulator;
uniform float AtlasSize;
uniform float GameTime;
uniform float TimeOffset;
uniform float Speed;
uniform float Intensity;
uniform float XFrequency;
uniform float YFrequency;
uniform float DistortionScale;

in vec4 vertexColor;
in vec2 texCoord0;
in vec2 distortionCoordinate;

out vec4 fragColor;

void main() {
    float atlasSize = max(AtlasSize, 1.0);
    vec2 atlasCoordinate = texCoord0 * atlasSize;
    vec2 tileCoordinate = floor(atlasCoordinate);
    vec2 tileUv = fract(atlasCoordinate);
    float time = GameTime * Speed + TimeOffset;
    float intensity = max(Intensity, 1.0);
    vec2 stableCoordinate = distortionCoordinate * DistortionScale;

    tileUv += vec2(
            cos(stableCoordinate.y * XFrequency + time),
            sin(stableCoordinate.x * YFrequency + time)
    ) / intensity;
    tileUv = clamp(tileUv, 0.0, 1.0);

    vec4 textureColor = texture(Sampler0, (tileCoordinate + tileUv) / atlasSize);
    if (textureColor.a == 0.0) {
        discard;
    }
    fragColor = transformColor(textureColor, LumiTransparency, vertexColor, ColorModulator);
}
