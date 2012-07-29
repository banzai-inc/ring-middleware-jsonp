(ns ring.middleware.jsonp.test
  (:import (java.io StringBufferInputStream))
  (:use [ring.middleware.jsonp]
        [ring.util.response :only (response content-type)]
        [clojure.test]))

(deftest test-json-with-padding
  (def test-bodies ["{}" (list "{}") (StringBufferInputStream. "{}")])

  (defn test-function [body]
    (let [handler  (constantly (content-type (response body) "application/json"))
          response ((wrap-json-with-padding handler) {:params {"callback" "f"}})]
      (is (= (get-in response [:headers "Content-Type"]) "application/javascript"))
      (is (= (:body response) "f({});"))))

  (dorun (map test-function test-bodies)))
