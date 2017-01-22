uniform vec3 point1;

varying vec3 colour;

void main(){
    //returning the color of the pixel (here solid blue)
    //- gl_FragColor is the standard GLSL variable that holds the pixel
    //color. It must be filled in the Fragment Shader.
    gl_FragColor = vec4(colour, 1);
}