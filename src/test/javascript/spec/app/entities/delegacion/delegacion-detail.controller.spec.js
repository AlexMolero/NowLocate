'use strict';

describe('Delegacion Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockDelegacion, MockExpedicion;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockDelegacion = jasmine.createSpy('MockDelegacion');
        MockExpedicion = jasmine.createSpy('MockExpedicion');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Delegacion': MockDelegacion,
            'Expedicion': MockExpedicion
        };
        createController = function() {
            $injector.get('$controller')("DelegacionDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'nowLocateApp:delegacionUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
