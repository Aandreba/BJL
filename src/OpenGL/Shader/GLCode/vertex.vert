#version 400

uniform mat4 project, view, transform;

in vec3 vp;
in vec3 inColor;

out vec3 exColor;

void main () {
    gl_Position = project * inverse(view) * transform * vec4(vp, 1.0);
    exColor = inColor;
}