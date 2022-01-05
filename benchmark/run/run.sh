#!/usr/bin/env bash

NB_HIT=200000

test(){
	curl "http://127.0.0.1:$1"
	date
	for (( i=0; i<$NB_HIT; i++ )); do
		curl "http://127.0.0.1:$1" >/dev/null 2>&1
	done
	date	
}

echo "Node test"
test "8222"

echo "Vertx test"
test "8111"
