//the global uniform World view projection matrix
//(more on global uniforms below)
uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_WorldMatrix;
uniform mat4 g_ViewProjectionMatrix;
uniform float g_Time;
//The attribute inPosition is the Object space position of the vertex
attribute vec3 inPosition;
attribute vec3 inNormal;
uniform vec3 m_point1;
uniform vec3 m_point2;
uniform vec3 m_point3;
uniform vec3 wind;
varying vec3 colour;


 float angleBetween(vec3 v1, vec3 v2) {
     return acos(dot(v1, v2)/(length(v1)*length(v2)));
 }

float GetDistance(vec3 rayOrigin, vec3 pt2, vec3 point){
         vec3 rayDir = pt2 - rayOrigin;
         float h = distance(rayOrigin,point);
         float angle = angleBetween(rayDir,point - rayOrigin);
         return (h * sin(angle));
     }

void main(){
    vec3 worldpoint = (g_WorldMatrix*vec4(inPosition, 1)).xyz;
    float d1 = GetDistance(m_point1, m_point2, worldpoint);
    float d2 = GetDistance(m_point2, m_point3, worldpoint);
    float d3 = GetDistance(m_point1, m_point3, worldpoint);
    float minimum = d1 * d2 * d3 / 200.0;
    colour = vec3(d1/10.0,d2/10.0, d3/10.0);

    vec3 newPosition = worldpoint +  vec3(1, 0, 0) * minimum;
    gl_Position = g_ViewProjectionMatrix * vec4(newPosition,1);
}

