'use strict';

describe('Camion Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockCamion, MockExpedicion;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockCamion = jasmine.createSpy('MockCamion');
        MockExpedicion = jasmine.createSpy('MockExpedicion');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Camion': MockCamion,
            'Expedicion': MockExpedicion
        };
        createController = function() {
            $injector.get('$controller')("CamionDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'nowLocateApp:camionUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
