#version 150

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;

uniform sampler2D Sampler2;
uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform float GameTime;
uniform float Speed;
uniform float TimeOffset;
uniform float XFrequency;
uniform float YFrequency;
uniform float Amplitude;

out vec4 vertexColor;
out vec2 texCoord0;

void main() {
    float time = GameTime * Speed + TimeOffset;
    vec3 displacedPosition = Position;
    displacedPosition.x += sin(Position.y * XFrequency + time) * Amplitude;
    displacedPosition.y += cos(Position.x * YFrequency + time) * Amplitude;

    vec4 position = ModelViewMat * vec4(displacedPosition, 1.0);
    gl_Position = ProjMat * position;
    vertexColor = Color * texelFetch(Sampler2, UV2 / 16, 0);
    texCoord0 = UV0;
}
