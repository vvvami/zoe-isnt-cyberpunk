#version 150

uniform sampler2D DiffuseSampler;
uniform float TintStrength;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);

    vec3 greener = vec3(color.r * 0.25, color.g, color.b * 0.25);
    vec3 finalRgb = mix(color.rgb, greener, TintStrength);

    fragColor = vec4(finalRgb, color.a);
}