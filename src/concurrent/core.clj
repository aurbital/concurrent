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

(defn -main
  "Experiment to test code for concurrent processes"
  [& args]
  ;--
  ; desired result: see the interleaving of printing between threads

  ; ******* use this one *******
  ; Atom REPL: outputs nil
  ; console REPL: prints here
  ; runs with no errors
  ; *** use this one: (.start (Thread. <some function that takes no arguments>))
  ; i.e. (.start (Thread. pl100)) and not
  ;      (.start (Thread. (pl100)))
  (.start (Thread. pl100))
  ; ***************************

  ; ******* or this one *******
  ; per feedback from stackoverflow
  ; Thread.run requires a function of no inputs
  ; so send it an anonymous function that takes care of all the input parameters you need
  ; can't do nested #, so just use (fn ..)
  ; this code outputs to console REPL and runs with no errors
  (.start (Thread. (fn [] (pl-n 100))))
  ; ***************************

  ; ******* these will also work *******
  ; note that Clojure functions already implement the Runnable interface
  ; so #(.run <func>) is not necessary
  (.start (Thread. #(.run (fn [] (pl-n 100)))))
  (.start (Thread. #(.run pl100))))
  ; ************************************
