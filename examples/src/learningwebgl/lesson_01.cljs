(ns learningwebgl.lesson-01
  (:require
    [mat4]
    [learningwebgl.common :refer [init-gl init-shaders get-perspective-matrix get-position-matrix]]
    [cljs-webgl.buffers :refer [create-buffer clear-color-buffer draw!]]
    [cljs-webgl.shaders :refer [get-attrib-location]]
    [cljs-webgl.constants :as const]
    [cljs-webgl.typed-arrays :as ta]))

(defn start []
  (let [canvas      (.getElementById js/document "canvas")
        gl          (init-gl canvas)
        shader-prog (init-shaders gl)
        triangle-vertex-buffer
                    (create-buffer gl
                      (ta/float32 [ 0.0,  1.0,  0.0,
                                   -1.0, -1.0,  0.0,
                                    1.0, -1.0,  0.0 ])
                      const/array-buffer
                      const/static-draw
                      3)

        square-vertex-buffer
                    (create-buffer gl
                      (ta/float32 [ 1.0,  1.0,  0.0,
                                   -1.0,  1.0,  0.0,
                                    1.0, -1.0,  0.0,
                                   -1.0, -1.0,  0.0])
                      const/array-buffer
                      const/static-draw
                      3)

        vertex-position-attribute (get-attrib-location gl shader-prog "aVertexPosition")]

    (clear-color-buffer gl 0.0 0.0 0.0 1.0)

    (draw!
      gl
      :shader shader-prog
      :draw-mode const/triangles
      :capabilities {const/depth-test true}
      :count (.-numItems triangle-vertex-buffer)
      :attributes [{:buffer triangle-vertex-buffer :location vertex-position-attribute}]
      :uniforms [{:name "uPMatrix" :type :mat4 :values (get-perspective-matrix gl)}
                 {:name "uMVMatrix" :type :mat4 :values (get-position-matrix [-1.5 0.0 -7.0])}])

    (draw!
      gl
      :shader shader-prog
      :draw-mode const/triangle-strip
      :count (.-numItems square-vertex-buffer)
      :attributes [{:buffer square-vertex-buffer :location vertex-position-attribute}]
      :uniforms [{:name "uPMatrix" :type :mat4 :values (get-perspective-matrix gl)}
                 {:name "uMVMatrix" :type :mat4 :values (get-position-matrix [1.5 0.0 -7.0])}])))
