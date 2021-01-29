(ns concurrent.core
  ;; note: no imports needed for java.util.Concurrent
  ; (:import [java.io File]
  ;          [java.util Date Stack]
  ;          [java.net Proxy URI])
  ;;          [java.util.concurrent])
  (:gen-class))

; function p100 is used for testing concurrent operations that don't have return values
; pl100 == "print loop 100"
; wanted a function that is guaranteed to end s.t. thread does not go into infinite loop
(defn pl100 []
  "Prints from 100 to 1"
  (loop [counter 100]
    (when (pos? counter)
      (do
        (Thread/sleep 100)
        (println (str "counter: " counter))
        (recur (dec counter))))))

(defn pl-n [n]
    "Prints from n to 1"
    (loop [counter n]
      (when (pos? counter)
        (do
          (Thread/sleep 100)
          (println (str "counter: " counter))
          (recur (dec counter))))))


; create an agent that keeps track of its position in a map using :x and :y
(defn create-ant
  "returns an agent that is represented as a position, which in turn is represented as a map with :x and :y"
  []
  (agent {:x 0 :y 0}))

(defn randomstep
  "This is the update function to be sent to ant (agent).\n Input: position as represented by a map\n Output: position as represented by a map"
  [{xpos :x ypos :y}]
  (letfn [(rand1 []
                 "random integer between -1 and +1, inclusive"
                 (+ (rand-int 3) -1))]
    {:x (+ xpos (rand1)) :y (+ ypos (rand1))}))

; (randomstep {:x 0 :y 0})
; (def ant (create-ant))
; (let [ant (create-ant)]
;   (send ant randomstep))

(defn ant-walk
  "Ant agent walk n steps"
  [n]
  (let [ant (create-ant)]
    (loop [counter n]
      (when (pos? counter)
        (do
          (send ant randomstep)
          ; (println (str "counter: " counter))
          (recur (dec counter)))))
    (print ant)))



        ; (send ant randomstep)

; (ant-walk 100)




(defn -main
  "Experiment to test code for concurrent processes"
  [& args]

  ;(.start (Thread. pl100))
  ;(.start (Thread. (fn [] (pl-n 100))))


  (.start (Thread. (fn [] (ant-walk 50))))
  (.start (Thread. (fn [] (ant-walk 50))))
  (.start (Thread. (fn [] (ant-walk 50))))
  (.start (Thread. (fn [] (ant-walk 50))))
  (.start (Thread. (fn [] (ant-walk 50)))))

; (-main)
