var PetsView = (function() {
    var dao;
    var peopleDao;
    var peopleById = {};

    var self;

    var formId = 'pets-form';
    var listId = 'pets-list';
    var formQuery = '#' + formId;
    var listQuery = '#' + listId;

    function PetsView(petsDao, peopleDaoInstance, formContainerId, listContainerId) {
        dao = petsDao;
        peopleDao = peopleDaoInstance;
        self = this;

        insertPetsForm($('#' + formContainerId));
        insertPetsList($('#' + listContainerId));

        this.init = function() {
            loadPeopleSelect(function() {
                dao.listPets(function(pets) {
                    $.each(pets, function(key, pet) {
                        appendToTable(pet);
                    });
                },
                function() {
                    alert('No ha sido posible acceder al listado de mascotas.');
                });
            });

            $(formQuery).submit(function(event) {
                var pet = self.getPetInForm();

                if (self.isEditing()) {
                    dao.modifyPet(pet,
                        function(pet) {
                            var row = $('#pet-' + pet.id);
                            $('#pet-' + pet.id + ' td.name').text(pet.name);
                            $('#pet-' + pet.id + ' td.breed').text(pet.breed);
                            $('#pet-' + pet.id + ' td.birthYear').text(pet.birthYear);
                            $('#pet-' + pet.id + ' td.personName').text(getPersonName(pet.personId));
                            row.attr('data-person-id', pet.personId);
                            self.resetForm();
                        },
                        showErrorMessage,
                        self.enableForm
                    );
                } else {
                    dao.addPet(pet,
                        function(pet) {
                            appendToTable(pet);
                            self.resetForm();
                        },
                        showErrorMessage,
                        self.enableForm
                    );
                }

                return false;
            });

            $('#btnPetsClear').click(this.resetForm);
        };

        this.getPetInForm = function() {
            var form = $(formQuery);
            return {
                'id': form.find('input[name="id"]').val(),
                'name': form.find('input[name="name"]').val(),
                'breed': form.find('input[name="breed"]').val(),
                'birthYear': form.find('input[name="birthYear"]').val(),
                'personId': form.find('select[name="personId"]').val()
            };
        };

        this.editPet = function(id) {
            var row = $('#pet-' + id);

            if (row !== undefined) {
                var form = $(formQuery);

                form.find('input[name="id"]').val(id);
                form.find('input[name="name"]').val(row.find('td.name').text());
                form.find('input[name="breed"]').val(row.find('td.breed').text());
                form.find('input[name="birthYear"]').val(row.find('td.birthYear').text());
                form.find('select[name="personId"]').val(row.attr('data-person-id'));

                $('input#btnPetsSubmit').val('Modificar');
            }
        };

        this.deletePet = function(id) {
            if (confirm('Está a punto de eliminar una mascota. ¿Está seguro de que desea continuar?')) {
                dao.deletePet(id,
                    function() {
                        $('tr#pet-' + id).remove();
                    },
                    showErrorMessage
                );
            }
        };

        this.isEditing = function() {
            return $(formQuery + ' input[name="id"]').val() != "";
        };

        this.disableForm = function() {
            $(formQuery + ' input, ' + formQuery + ' select').prop('disabled', true);
        };

        this.enableForm = function() {
            $(formQuery + ' input, ' + formQuery + ' select').prop('disabled', false);
        };

        this.resetForm = function() {
            $(formQuery)[0].reset();
            $(formQuery + ' input[name="id"]').val('');
            $('#btnPetsSubmit').val('Crear');
        };
    };

    var insertPetsList = function(parent) {
        parent.append(
            '<table id="' + listId + '" class="table">\
                <thead>\
                    <tr class="row">\
                        <th class="col-sm-3">Nombre</th>\
                        <th class="col-sm-3">Raza</th>\
                        <th class="col-sm-2">Nacimiento</th>\
                        <th class="col-sm-2">Persona</th>\
                        <th class="col-sm-2">&nbsp;</th>\
                    </tr>\
                </thead>\
                <tbody>\
                </tbody>\
            </table>'
        );
    };

    var insertPetsForm = function(parent) {
        parent.append(
            '<form id="' + formId + '" class="mb-5 mb-10">\
                <input name="id" type="hidden" value=""/>\
                <div class="row g-2">\
                    <div class="col-sm-3">\
                        <input name="name" type="text" value="" placeholder="Nombre" class="form-control" required/>\
                    </div>\
                    <div class="col-sm-3">\
                        <input name="breed" type="text" value="" placeholder="Raza" class="form-control" required/>\
                    </div>\
                    <div class="col-sm-2">\
                        <input name="birthYear" type="number" value="" placeholder="Año" class="form-control" required/>\
                    </div>\
                    <div class="col-sm-2">\
                        <select id="pets-person-select" name="personId" class="form-control" required>\
                            <option value="" selected disabled>Selecciona persona</option>\
                        </select>\
                    </div>\
                    <div class="col-sm-2">\
                        <input id="btnPetsSubmit" type="submit" value="Crear" class="btn btn-primary" />\
                        <input id="btnPetsClear" type="reset" value="Limpiar" class="btn" />\
                    </div>\
                </div>\
            </form>'
        );
    };

    var loadPeopleSelect = function(done) {
        done = typeof done !== 'undefined' ? done : function() {};

        peopleDao.listPeople(function(people) {
            var select = $(formQuery + ' select[name="personId"]');
            select.find('option:not([disabled])').remove();
            peopleById = {};

            $.each(people, function(key, person) {
                peopleById[person.id] = person;
                select.append(
                    '<option value="' + person.id + '">' + person.name + ' ' + person.surname + '</option>'
                );
            });

            done();
        }, function() {
            alert('No ha sido posible cargar el listado de personas para asociar mascota.');
        });
    };

    var getPersonName = function(personId) {
        var person = peopleById[personId];
        return person !== undefined ? person.name + ' ' + person.surname : '-';
    };

    var createPetRow = function(pet) {
        return '<tr id="pet-'+ pet.id +'" class="row" data-person-id="' + pet.personId + '">\
            <td class="name col-sm-3">' + pet.name + '</td>\
            <td class="breed col-sm-3">' + pet.breed + '</td>\
            <td class="birthYear col-sm-2">' + pet.birthYear + '</td>\
            <td class="personName col-sm-2">' + getPersonName(pet.personId) + '</td>\
            <td class="col-sm-2">\
                <a class="edit btn btn-primary" href="#">Editar</a>\
                <a class="delete btn btn-warning" href="#">Eliminar</a>\
            </td>\
        </tr>';
    };

    var showErrorMessage = function(jqxhr, textStatus, error) {
        alert(textStatus + ": " + error);
    };

    var addRowListeners = function(pet) {
        $('#pet-' + pet.id + ' a.edit').click(function() {
            self.editPet(pet.id);
        });

        $('#pet-' + pet.id + ' a.delete').click(function() {
            self.deletePet(pet.id);
        });
    };

    var appendToTable = function(pet) {
        $(listQuery + ' > tbody:last')
            .append(createPetRow(pet));
        addRowListeners(pet);
    };

    return PetsView;
})();