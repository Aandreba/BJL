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
    worldCoord = transform * vec4(vp, 1);
    gl_Position = project * view * worldCoord;

    outTexCoord = texCoord;
    outNormal = mat3(transpose(inverse(transform))) * normal;
}