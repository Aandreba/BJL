#version 400

// UNIFORMS //
uniform mat4 transform;
uniform float project;

in vec3 vp;
in vec2 texCoord;
in vec3 normal;
out vec2 outTexCoord;

void main () {
    gl_Position = transform * vec4(vp, 1) * vec4(project, 1, 1, 1);
    outTexCoord = texCoord;
}