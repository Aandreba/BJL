#version 400

const float PI = 3.14159;
const int MAX_POINTS = 5;

/*
    FUN FACT
    max(sin(x), cos(x)) * min(sin(x), cos(x)) = sin(2x) / 2
    sin(x) = -cos(x + pi / 2)
    cos(x) = -sin(x - pi / 2)

    cos(x) [-pi, 0] = 0.95 - 0.52x^2 - 0.1x^3 (max error: -0.088)
*/

// STRUCTS //
struct PointLight {
    vec3 pos;
    vec3 color;
    float intensity;
};

struct DirectionalLight {
    vec3 color;
    vec3 dir;
    float intensity;
};

struct SpotLight {
    vec3 pos;
    vec3 dir;
    vec3 color;
    float cutoff;
    float intensity;
};

// UNIFORMS //
uniform PointLight points[MAX_POINTS];
uniform DirectionalLight directionals[MAX_POINTS];
uniform SpotLight spots[MAX_POINTS];

uniform sampler2D textureSampler;
uniform vec3 defColor;
uniform int hasTexture;
uniform float reflectance;

// FUNCTIONS //
vec3 calcLight (vec3 lightColor, float lightIntensity, vec3 dir, vec3 pos, vec3 normal, vec3 color) {
    // Diffuse
    float factorD = max(dot(normal, dir), 0);
    vec3 diffuse = color * lightColor * lightIntensity * factorD;

    // Specular
    vec3 camDir = normalize(-pos);
    vec3 reflected = normalize(reflect(-dir, normal));
    float factorS = max(dot(camDir, reflected), 0);
    vec3 specular = color * lightIntensity * pow(factorS, 32) * reflectance * lightColor;

    return specular + diffuse;
}

vec3 calcLightPoint (PointLight light, vec3 pos, vec3 normal, vec3 color) {
    vec3 dist = light.pos - pos;
    vec3 dir = normalize(dist);
    return calcLight (light.color, light.intensity, dir, pos, normal, color) / pow(length(dist),2);
}

vec3 calcLightDir (DirectionalLight light, vec3 pos, vec3 normal, vec3 color) {
    return calcLight(light.color, light.intensity, light.dir, pos, normal, color);
}

vec3 calcLightSpot (SpotLight light, vec3 pos, vec3 normal, vec3 color) {
    vec3 dist = light.pos - pos;
    vec3 dir = normalize(dist);

    float c = distance(dir, light.dir);
    float alpha = (2 - (c * c)) / 2;
    vec3 outColor = vec3(0, 0, 0);

    if (alpha > light.cutoff) {
        outColor = calcLight(light.color, light.intensity, dir, pos, normal, color) / pow(length(dist),2);
        outColor *= 1.0 - (1.0 - alpha) / (1.0 - light.cutoff);
    }

    return outColor;
}

in vec2 outTexCoord;
in vec4 worldCoord;
in vec3 outNormal;
out vec4 color;

void main () {
    vec3 startColor = defColor;
    if (hasTexture == 1) {
        startColor *= texture(textureSampler, outTexCoord).xyz;
    }

    vec3 delta = vec3(0, 0, 0);
    for (int i=0;i<MAX_POINTS;i++) {
        PointLight point = points[i];
        DirectionalLight dir = directionals[i];
        SpotLight spot = spots[i];

        delta += calcLightPoint(point, worldCoord.xyz, outNormal, startColor);
        delta += calcLightDir(dir, worldCoord.xyz, outNormal, startColor);
        delta += calcLightSpot(spot, worldCoord.xyz, outNormal, startColor);
    }

    color = vec4(startColor + startColor * delta, 1);
}