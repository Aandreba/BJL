#version 400
const int MAX_POINTS = 5;

// STRUCTS //
struct LightPoint {
    vec3 pos;
    vec3 color;
    float intensity;
};

// FUNCTIONS //
vec3 applyLightPoint (LightPoint light, vec3 pos) {
    float dist = distance(light.pos, pos);
    return light.color * light.intensity / pow(dist, 2);
}

uniform LightPoint points[MAX_POINTS];
uniform sampler2D textureSampler;
uniform vec3 defColor;
uniform int hasTexture;

in vec2 outTexCoord;
in vec4 worldCoord;
in vec3 outNormal;
out vec4 color;

void main () {
    vec3 startColor;
    if (hasTexture == 1) {
        startColor = texture(textureSampler, outTexCoord).xyz;
    } else {
        startColor = defColor;
    }

    vec3 delta = vec3(0, 0, 0);
    for (int i=0;i<MAX_POINTS;i++) {
        LightPoint light = points[i];
        delta += applyLightPoint(light, worldCoord.xyz);
    }

    startColor *= delta;
    color = vec4(startColor, 1);
}