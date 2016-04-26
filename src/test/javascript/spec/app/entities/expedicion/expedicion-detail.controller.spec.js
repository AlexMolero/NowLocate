'use strict';

describe('Expedicion Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockExpedicion, MockCamion, MockDelegacion;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockExpedicion = jasmine.createSpy('MockExpedicion');
        MockCamion = jasmine.createSpy('MockCamion');
        MockDelegacion = jasmine.createSpy('MockDelegacion');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Expedicion': MockExpedicion,
            'Camion': MockCamion,
            'Delegacion': MockDelegacion
        };
        createController = function() {
            $injector.get('$controller')("ExpedicionDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'nowLocateApp:expedicionUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
