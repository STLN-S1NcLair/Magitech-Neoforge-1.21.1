#version 150

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform float GameTime;
uniform float Width;
uniform float Height;
uniform float MarginX;
uniform float MarginY;
uniform float BorderSize;
uniform float TileWidth;
uniform float TileHeight;
uniform float TextureSize;
uniform float PixelScale;
uniform float TextureWidth;
uniform float TextureHeight;
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

bool mapSliceCoordinate(
        float coordinate,
        float size,
        float displacement,
        float textureSize,
        float borderSize,
        float tileSize,
        out float sourceCoordinate
) {
    float sampleCoordinate = coordinate - displacement;

    if (sampleCoordinate < 0.0 || sampleCoordinate >= size) {
        return false;
    }
    if (sampleCoordinate < borderSize) {
        sourceCoordinate = sampleCoordinate;
        return true;
    }
    if (sampleCoordinate >= size - borderSize) {
        sourceCoordinate = textureSize - (size - sampleCoordinate);
        return true;
    }

    sourceCoordinate = borderSize + mod(sampleCoordinate - borderSize, tileSize);
    return true;
}

void main() {
    float time = GameTime * Speed + TimeOffset;
    vec2 expandedSize = vec2(Width + MarginX * 2.0, Height + MarginY * 2.0);
    vec2 targetPosition = texCoord0 * expandedSize - vec2(MarginX, MarginY);
    vec2 uv = targetPosition / vec2(Width, Height);
    vec2 distortion = vec2(
            cos(uv.y * XFrequency + time),
            sin(uv.x * YFrequency + time)
    ) / Intensity * vec2(Width, Height);

    float textureWidth = TextureWidth * PixelScale;
    float textureHeight = TextureHeight * PixelScale;
    float scaledBorderSize = BorderSize * PixelScale;
    float scaledTileWidth = TileWidth * PixelScale;
    float scaledTileHeight = TileHeight * PixelScale;
    float sourceX;
    float sourceY;
    bool validX = mapSliceCoordinate(
            targetPosition.x,
            Width,
            distortion.x,
            textureWidth,
            scaledBorderSize,
            scaledTileWidth,
            sourceX
    );
    bool validY = mapSliceCoordinate(
            targetPosition.y,
            Height,
            distortion.y,
            textureHeight,
            scaledBorderSize,
            scaledTileHeight,
            sourceY
    );
    if (!validX || !validY) {
        discard;
    }

    float frameCount = max(FrameCount, 1.0);
    float frameIndex = mod(FrameIndex, frameCount);
    float frameHeight = textureHeight;
    float totalTextureHeight = frameHeight * frameCount;
    float halfTexel = PixelScale * 0.5;
    float clampedSourceX = clamp(sourceX, halfTexel, textureWidth - halfTexel);
    float clampedSourceY = clamp(sourceY, halfTexel, frameHeight - halfTexel);
    vec2 sourceUV = vec2(
            clampedSourceX / textureWidth,
            (clampedSourceY + frameIndex * frameHeight) / totalTextureHeight
    );
    vec2 nextSourceUV = vec2(
            clampedSourceX / textureWidth,
            (clampedSourceY + mod(frameIndex + 1.0, frameCount) * frameHeight) / totalTextureHeight
    );
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
