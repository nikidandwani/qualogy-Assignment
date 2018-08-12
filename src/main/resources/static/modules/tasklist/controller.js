/**
 *
 */
app.controller("TodoController", function (TodoService, $scope, $rootScope) {
    console.log("hello controller")

    var self = this;
    $scope.todos = [];
    self.todo = {};
    self.editMode = false;
    $scope.updateDescription = false;
    $scope.editingTodo = {};
    $rootScope.serverError = false;
    $("#success-alert").hide();
    self.findAllTodos = function () {
        $scope.todos = [];
        TodoService.findAllTodos().then(function (data) {
            $scope.todos = data;
        });
    };

    self.addTodo = function () {

        TodoService.addTodo(self.todo).then(function (data) {
            self.findAllTodos();

            $("#success-alert").fadeTo(2000, 500).slideUp(500, function () {
                $("#success-alert").slideUp(500);
            });
        });

        self.todo = {};
    };

    self.deleteTodo = function (id) {
        TodoService.deleteTodo(id).then(function (data) {
            self.findAllTodos();
        });
    };

    self.editTodo = function (todo) {
        TodoService.updateTodo(todo).then(function (data) {
            //self.findAllTodos();
        });
        self.editMode = false;
    };

    self.openDeleteModal = function () {
        console.log("modal button clicker");
        $('#exampleModal').modal('show')
    }

    self.deleteAllTodos = function () {
        TodoService.deleteAllTodos().then(function (data) {
            self.findAllTodos();
        });
    }

    self.updateDescription = function (todo) {
        $scope.updateDescription = true;
        Object.assign($scope.editingTodo, todo);
    }


    self.finishEditing = function (todo) {
        $scope.updateDescription = false;
        $scope.editingTodo = {};
        self.editTodo(todo);
    }
    self.findAllTodos();
});


