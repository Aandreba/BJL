#version 400

// UNIFORMS //
uniform mat4 project, view, transform;

in vec3 vp;
in vec2 texCoord;
in vec3 normal;

out vec4 worldCoord;
out vec2 outTexCoord;
out vec3 outNormal;

void main () {
    vec4 pos = vec4(vp, 1);
    mat4 worldMatrix = view * transform;

    worldCoord = transform * pos;
    gl_Position = project * worldMatrix * pos;

    outTexCoord = texCoord;
    outNormal = normal;
}