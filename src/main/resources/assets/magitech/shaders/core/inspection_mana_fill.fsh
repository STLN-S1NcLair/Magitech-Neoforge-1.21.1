#version 150

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform float GameTime;
uniform float Width;
uniform float Height;
uniform float MarginX;
uniform float MarginY;
uniform float TextureWidth;
uniform float TextureHeight;
uniform float PixelScale;
uniform float Speed;
uniform float TimeOffset;
uniform float Intensity;
uniform float XFrequency;
uniform float YFrequency;
uniform float FrameCount;
uniform float FrameIndex;
uniform float FrameBlend;

in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    float time = GameTime * Speed + TimeOffset;
    vec2 expandedSize = vec2(Width + MarginX * 2.0, Height + MarginY * 2.0);
    vec2 targetPosition = texCoord0 * expandedSize - vec2(MarginX, MarginY);
    vec2 uv = targetPosition / vec2(Width, Height);
    vec2 distortion = vec2(
            cos(uv.y * XFrequency + time),
            sin(uv.x * YFrequency + time)
    ) / Intensity * vec2(Width, Height);
    vec2 samplePosition = targetPosition - distortion;

    if (samplePosition.x < 0.0 || samplePosition.x >= Width
            || samplePosition.y < 0.0 || samplePosition.y >= Height) {
        discard;
    }

    float frameCount = max(FrameCount, 1.0);
    float frameWidth = TextureWidth * PixelScale;
    float frameHeight = TextureHeight * PixelScale;
    float sourceX = mod(samplePosition.x, frameWidth);
    float sourceY = mod(samplePosition.y, frameHeight);
    float halfTexel = PixelScale * 0.5;
    sourceX = clamp(sourceX, halfTexel, frameWidth - halfTexel);
    sourceY = clamp(sourceY, halfTexel, frameHeight - halfTexel);

    float frameIndex = mod(FrameIndex, frameCount);
    float frameY = (sourceY + frameIndex * frameHeight) / (frameHeight * frameCount);
    float nextFrameY = (sourceY + mod(frameIndex + 1.0, frameCount) * frameHeight)
            / (frameHeight * frameCount);
    vec2 sourceUV = vec2(sourceX / frameWidth, frameY);
    vec2 nextSourceUV = vec2(sourceX / frameWidth, nextFrameY);
    vec4 sampledColor = mix(
            texture(Sampler0, sourceUV),
            texture(Sampler0, nextSourceUV),
            clamp(FrameBlend, 0.0, 1.0)
    );
    vec4 color = sampledColor * vertexColor * ColorModulator;
    if (color.a == 0.0) {
        discard;
    }
    fragColor = color;
}
