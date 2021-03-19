#version 400

uniform mat4 project, view, transform;

in vec3 vp;
in vec2 texCoord;
in vec3 normals;

out vec2 outTexCoord;

void main () {
    gl_Position = project * view * transform * vec4(vp, 1.0);
    outTexCoord = texCoord;
}