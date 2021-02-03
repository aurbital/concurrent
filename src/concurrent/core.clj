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


(defn rand1
  "returns a random integer between -1 and 1"
  []
  (+ (rand-int 3) -1))

;;; --------------------------------------
;;; traveler "class" and associated "methods"

(defn create-traveler
  "function: create-traveler \n semantics: returns a newly-instantiated traveling entity \n representation: a dcc (data-code-compound) data structure"
 ([]
  {:path (agent [[0 0]])})
 ([[x y]]
  {:path (agent [[x y]])})
 ([x y]
  {:path (agent [[x y]])}))

(defn pos
  "function: pos \n semantics: returns the current position of a traveler \n representation: the position is represented as a vector [x y] inside :path which is a vector of vectors ([[xt yt] [x(t-1) y(t-1)] [x(t-2) y(t-2)] ...]) wrapped inside an agent"
  [traveler]
  (first @(:path traveler)))

(defn xpos
  "function: xpos \n semantics: returns the current x-position of a traveler \n representation: the first element of the position vector [x y]"
  [traveler]
  (first (pos traveler)))

(defn ypos
  "function: ypos \n semantics: returns the current y-position of a traveler \n representation: the second element of the position vector [x y]"
  [traveler]
  (second (pos traveler)))

(defn randstep
  "function: randstep \n semantics: updates traveler to take a random integer-sized step of at most step-size 1 \n representation: sends update function to :path, returns updated :path agent (not updated traveler); can discard the return value if only care about traveler"
  [traveler]
  (let [x (xpos traveler)
        y (ypos traveler)
        xnew (+ (rand1) x)
        ynew (+ (rand1) y)]
    (send (:path traveler) (fn [v] (cons [xnew ynew] v)))))

; ;; conj or cons?
; (def aa [[0 0]])
; (print (conj (conj aa [1 1]) [2 2])) ; --> [[0 0] [1 1] [2 2]]
; (print (cons [2 2] (cons [1 1] aa))) ; --> ([2 2] [1 1] [0 0])
(defn randwalk
  "loops through randstep for a total of n times"
  [traveler n]
  (loop [counter n]
    (when (pos? counter)
      (do
        (randstep traveler)
        (recur (dec counter)))))
  traveler)

(let [t (create-traveler)]
  (randwalk t 10))

; (let [t (create-traveler [0 0])]
;  (println (str "path: " @(:path t)))
;  (println (str "current position: " (pos t)))
;  (println (str "current x: " (xpos t)))
;  (println (str "current y: " (ypos t)))
;  (randstep t)
;  (randstep t)
;  (randstep t)
;  t)

;;; --------------------------------------


(defn -main
  "Experiment to test code for concurrent processes"
  [& args]

  ;(.start (Thread. pl100))
  ;(.start (Thread. (fn [] (pl-n 100))))
  (let [rw (fn []
             (let [t (create-traveler)]
              (randwalk t 100)))]
    (rw)))


(-main)
