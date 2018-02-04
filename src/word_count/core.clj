;; just assume we've already parsed the text elsewhere
(defn get-words [text] (re-seq #"\w+" text))

;; sequences in clojure are "lazy" so the size of the input shouldn't matter
(defn count-words-sequential [pages]
  (frequencies (mapcat get-words pages)))

;; parallel word count - takes advantage of the built-in merge-with function
;; provided by clojure
(defn count-words-parallel [pages]
  (reduce (partial merge-with +)
          (pmap #(frequencies (get-words %)) pages)))

;; batched solution - results in 3.2x speedup
(defn count-words-batch [pages]
  (reduce (partial merge-with +)
          (pmap count-words-sequential (partition-all 100 pages))))