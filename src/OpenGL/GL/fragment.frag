#version 400

uniform sampler2D textureSampler;

in vec2 outTexCoord;
out vec4 color;

void main () {
    color = vec4(0.5, 0.5, 0.5, 1);
    //color = texture(textureSampler, outTexCoord);
}