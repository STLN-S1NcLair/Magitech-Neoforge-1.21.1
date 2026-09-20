#version 150

in vec3 Position;
in vec4 Color;
in vec2 UV0;

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
    vec4 position = ModelViewMat * vec4(Position, 1.0);
    position.x += sin(position.y * XFrequency + time) * Amplitude;
    position.y += cos(position.x * YFrequency + time) * Amplitude;

    gl_Position = ProjMat * position;
    vertexColor = Color;
    texCoord0 = UV0;
}
