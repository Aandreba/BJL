#version 400

in vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D textureSampler;
uniform int hasTexture;
uniform vec3 defColor;

void main() {
    vec4 startColor = vec4(defColor, 1);
    if (hasTexture == 1) {
        startColor *= texture(textureSampler, outTexCoord);
    }

    fragColor = startColor;
}