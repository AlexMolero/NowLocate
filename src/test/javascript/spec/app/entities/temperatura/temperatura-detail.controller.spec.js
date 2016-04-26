'use strict';

describe('Temperatura Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockTemperatura, MockExpedicion;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockTemperatura = jasmine.createSpy('MockTemperatura');
        MockExpedicion = jasmine.createSpy('MockExpedicion');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Temperatura': MockTemperatura,
            'Expedicion': MockExpedicion
        };
        createController = function() {
            $injector.get('$controller')("TemperaturaDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'nowLocateApp:temperaturaUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
