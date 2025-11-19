# Envío de peticiones HTTP

## `curl`

Ejemplos `GET`:
```shell
curl -v http://localhost:8080/
curl -v http://localhost:8080/articuloes
curl -v http://localhost:8080/pedidoes/6
curl -v http://localhost:8080/clientes?sort=nombre
```

Ejemplo `POST`:
```shell
curl -v --header 'Content-Type: application/json' --data '{"nombre": "tubo acero POST","descripcion": "tubo de acero enviado por POST","precioUnitario": 123.456,"familia": "familias/1"}' --request POST http://localhost:8080/articuloes
```

## Extensiones

Las siguientes extensiones permiten gestionar colecciones de peticiones y ejecutarlas desde las propias extensiones:

- VS Code: [Thunder Client](https://marketplace.visualstudio.com/items?itemName=rangav.vscode-thunder-client)
- Firefox: [RESTED](https://addons.mozilla.org/en-US/firefox/addon/rested)
- Chrome: [Talend API Tester](https://chromewebstore.google.com/detail/talend-api-tester-free-ed/aejoelaoggembcahagimdiliamlcdmfm?pli=1)

Por ejemplo, la petición `POST` anterior se vería de la siguiente manera en Thunder Client (VS Code).

![Thunder Client (VS Code)](vscode.png "Thunder Client (VS Code)")
