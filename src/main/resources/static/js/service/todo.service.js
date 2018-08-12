app.service("TodoService",function($http, $rootScope, API_ENDPOINT){
	this.findAllTodos = function(){
		return $http.get(API_ENDPOINT.TODOS).then(function(response) {
            $rootScope.serverError=false;
			return response.data;
		}, function(errResponse) {
            $rootScope.serverError=false;
			console.log("An exception occured-get", errResponse);
			return errResponse;
		});
	}
	
	this.addTodo = function(todo){
		return $http.post(API_ENDPOINT.TODOS, todo).then(function(response) {
            $rootScope.serverError=false;
			return response.data;
		}, function(errResponse) {
            $rootScope.serverError=false;
			console.log("An exception occured-post", errResponse);
			return errResponse;
		});
	}
	
	this.deleteTodo = function(id){
		return $http.delete(API_ENDPOINT.TODOS + "/" + id).then(function(response) {
            $rootScope.serverError=false;
			return response.data;
		}, function(errResponse) {
            $rootScope.serverError=false;
			console.log("An exception occured-delete", errResponse);
			return errResponse;
		});
	}
	
	this.updateTodo = function(todo){

      /*  $http({
            url: API_ENDPOINT.TODOS + "/" + todo.id,
            method: 'PUT',
            contentType: 'application/json; charset=utf-8',
            data: todo
        }).then(function (response) { // this is the success
            return response.data;
        }, function (error) {  // this is the error
            return error.data;
        });*/

		return $http.patch(API_ENDPOINT.TODOS + "/" + todo.id, todo).then(function(response) {
            $rootScope.serverError=false;
		    return response.data;
		}, function(errResponse) {
            $rootScope.serverError=false;
			console.log("An exception occured-update", errResponse);
			return errResponse;
		});
	}

    this.deleteAllTodos = function(){
        return $http.delete(API_ENDPOINT.TODOS).then(function(response) {
            $rootScope.serverError=false;
            return response.data;
        }, function(errResponse) {
            $rootScope.serverError=true;
            console.log("An exception occured-delete all", errResponse);
            return errResponse;
        });
    }
});
