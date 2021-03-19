#version 400

uniform sampler2D textureSampler;
uniform vec4 defColor;
uniform int useColor;

in vec2 outTexCoord;
out vec4 color;

void main () {
    if (useColor == 1) {
        color = defColor;
    } else {
        color = texture(textureSampler, outTexCoord);
    }
}