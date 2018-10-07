function endpoint(name, description, method, url, parameters = [], result = undefined, errors = []) {
    return {
        name,
        description,
        method,
        url,
        parameters,
        result,
        errors
    };
}

function result(type, description) {
    return {
        type,
        description
    }
}

function parameter(name, type, description) {
    return {
        name,
        type,
        description
    }
}

function structure(name, description, json) {
    return {
        name,
        description,
        json
    }
}

const structures = [
    structure('Person', 'Represents a person in the system.', '{\n' +
        '  "id": 1,\n' +
        '  "firstName": "Jonas",\n' +
        '  "lastName": "Behrendt",\n' +
        '  "email": "jonas-behrendt-1183@protonmail.dk",\n' +
        '  "phones": [\n' +
        '    {\n' +
        '      "id": 1,\n' +
        '      "number": "94944147",\n' +
        '      "description": "Mobil"\n' +
        '    },\n' +
        '    {\n' +
        '      "id": 2,\n' +
        '      "number": "39833016",\n' +
        '      "description": "Mobil"\n' +
        '    },\n' +
        '    {\n' +
        '      "id": 3,\n' +
        '      "number": "64348625",\n' +
        '      "description": "Arbejde"\n' +
        '    }\n' +
        '  ],\n' +
        '  "address": {\n' +
        '    "street": "Ryllevej",\n' +
        '    "information": "110",\n' +
        '    "city": {\n' +
        '      "id": 733,\n' +
        '      "zipCode": "4632",\n' +
        '      "name": "Bjæverskov"\n' +
        '    }\n' +
        '  }\n' +
        '}'),
    structure('CountStructure', 'Contains some requested count of a resource.', '{\n' +
        '  "count": {n}\n' +
        '}')
];

const personNotFoundExceptionBody ='{\n' +
    '  "exception": "PersonNotFoundException",\n' +
    '  "message": "No person with provided id {id}.",\n' +
    '  "responseCode": 404,\n' +
    '  "debug": true\n' +
    '}';
const endpoints = [
    endpoint('getPersons', 'Retrieves a complete list of the persons in the system', 'GET', 'persons', [], result(arrayOf('Person'), "The complete list of the persons in the system.")),
    endpoint('getPersonsPaginated', 'Retrieves a single page of the persons in the system', 'GET', 'paginated/{pageSize}/{pageNumber}', [parameter('pageSize', 'int', 'The number of results on each page'), parameter('pageNumber', 'int', 'The page to return, starts at 1.')], result(arrayOf('Person'), "The persons on the retrieved page.")),
    endpoint('getPersonsByAddress', 'Retrieves the persons in the provided street and/or city.', 'GET', 'persons/street/{street}/city/{city}', [parameter('street', 'string', 'The name of the street to retrieve persons from. Ignored when parameter value is empty.'), parameter('city', 'int', 'The id of the city to retrieve persons from. Ignored when parameter value is empty.')], result(arrayOf('Person'), "The persons matching the provided criteria.")),
    endpoint('countPersons', 'Retrieves the number of persons in the system', 'GET', 'persons/count', result(structureLink('CountStructure'), 'A CountStructure containing the number of persons in the system.')),
    endpoint('getPersonWithId', 'Retrieves the person with the provided id', 'GET', 'persons/{id}', [parameter('id', 'int', 'The id of the person to retrieve.')], result(structureLink('Person'), 'The person with the provided id.'), [error('When no person with the provided id exists.', personNotFoundExceptionBody)])
];

const structureTarget = document.getElementById('structures-target');
structures.forEach(structure => {

    const row = document.createElement("div");
    row.classList.add('row');
    row.classList.add('endpoint-container');

    const anchor = document.createElement('a');
    anchor.setAttribute('id', 'structure-' + structure.name);
    row.appendChild(anchor);

    const col = document.createElement('div');
    col.classList.add('col');
    col.classList.add('s12');
    row.appendChild(col);

    const name = document.createElement('h2');
    name.classList.add('name');
    name.innerText = structure.name;
    col.appendChild(name);

    const description = document.createElement('p');
    description.classList.add('description');
    description.innerText = structure.description;
    col.appendChild(description);

    const json = document.createElement('pre');
    json.classList.add('json');
    json.innerText = structure.json;
    col.appendChild(json);

    structureTarget.appendChild(row);
});

const endpointTarget = document.getElementById('endpoints-target');

endpoints.forEach(endpoint => {

    const row = document.createElement("div");
    row.classList.add('row');
    row.classList.add('endpoint-container');

    const col = document.createElement('div');
    col.classList.add('col');
    col.classList.add('s12');
    row.appendChild(col);

    const name = document.createElement('h3');
    name.classList.add('name');
    name.innerHTML = endpoint.name;
    col.appendChild(name);

    const resource = document.createElement('div');
    const method = document.createElement('span');
    const url = document.createElement('span');

    resource.classList.add('resource');
    method.classList.add('method');
    url.classList.add('url');

    method.innerText = endpoint.method;
    url.innerText = endpoint.url;

    resource.appendChild(method);
    resource.appendChild(url);
    col.appendChild(resource);

    const description = document.createElement('p');
    description.innerHTML = endpoint.description;
    col.appendChild(description);

    if (endpoint.parameters.length > 0) {
        const h4 = document.createElement('h4');
        h4.innerText = 'Parameters';
        h4.classList.add('parameters-header');
        const table = document.createElement("table");
        table.classList.add('parameters-table');

        const headers = ['Name', 'Type', 'Description'];
        const headersTr = document.createElement('tr');
        headers.forEach(header => {
            const th = document.createElement('th');
            th.innerText = header;
            headersTr.appendChild(th);
        });

        table.appendChild(headersTr);

        endpoint.parameters.forEach(parameter => {
            const tr = document.createElement('tr');
            tr.classList.add('parameter-tr')

            const parameterName = document.createElement('td');
            const parameterType = document.createElement('td');
            const parameterDescription = document.createElement('td');

            parameterName.innerText = parameter.name;
            parameterType.innerText = parameter.type;
            parameterDescription.innerText = parameter.description;

            parameterName.classList.add('parameter-name');
            parameterType.classList.add('parameter-type');
            parameterDescription.classList.add('parameter-description');

            tr.appendChild(parameterName);
            tr.appendChild(parameterType);
            tr.appendChild(parameterDescription);
            table.appendChild(tr);
        });

        col.appendChild(h4);
        col.appendChild(table);
    }

    if (endpoint.result) {
        const h4 = document.createElement('h4');
        h4.innerText = 'Returns';
        h4.classList.add('result-header');
        col.appendChild(h4);

        const div = document.createElement('div');
        div.classList.add('result-contents');
        col.appendChild(div);

        const type = document.createElement('span');
        type.classList.add('result-type');
        type.innerHTML = endpoint.result.type;
        div.appendChild(type);

        const description = document.createElement('span');
        description.classList.add('result-description');
        description.innerHTML = endpoint.result.description;
        div.appendChild(description);
    }

    if(endpoint.errors.length > 0){
        const h4 = document.createElement('h4');
        h4.innerText = 'Errors';
        h4.classList.add('errors-header');
        const table = document.createElement("table");
        table.classList.add('errors-table');

        const headers = ['When', 'Returns'];
        const headersTr = document.createElement('tr');
        headers.forEach(header => {
            const th = document.createElement('th');
            th.innerText = header;
            headersTr.appendChild(th);
        });

        table.appendChild(headersTr);
        col.appendChild(h4);
        col.appendChild(table);

        endpoint.errors.forEach(error => {
            const tr = document.createElement('tr');
            tr.classList.add('error-tr')

            const errorWhen = document.createElement('td');
            const errorResult = document.createElement('td');
            const errorResultPre = document.createElement('pre');

            errorWhen.innerText = error.when;
            errorResultPre.innerHTML = error.json;

            errorWhen.classList.add('error-when');
            errorResult.classList.add('error-result');

            errorResult.appendChild(errorResultPre);
            tr.appendChild(errorWhen);
            tr.appendChild(errorResult);
            table.appendChild(tr);
        });
    }

    endpointTarget.appendChild(row);
});

function structureLink(structureName, text = structureName) {
    return `<a href='#structure-${structureName}'>${structureName}</a>`;
}

function arrayOf(structureName, text = structureName) {
    return structureLink(structureName, text) + '[]';
}

function error(when, json) {
    return {
        when,
        json
    }
}