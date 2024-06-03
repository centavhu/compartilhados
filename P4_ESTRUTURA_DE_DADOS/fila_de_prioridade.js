class ListaDePrioridade {
  constructor() {
    this.filaP = [];
  }

  indexPai(index) {
    return Math.floor((index - 1) / 2);
  }

  indexFilhoEsquerda(index) {
    return 2 * index + 1;
  }

  indexFilhoDireita(index) {
    return 2 * index + 2;
  }

  trocar(index1, index2) {
    [this.filaP[index1], this.filaP[index2]] = [
      this.filaP[index2],
      this.filaP[index1],
    ];
  }

  inserir(element) {
    this.filaP.push(element);
    this.empilharEmCima();
  }

  empilharEmCima() {
    let index = this.filaP.length - 1;

    while (
      index > 0 &&
      this.filaP[index].priority < this.filaP[this.indexPai(index)].priority
    ) {
      this.trocar(index, this.indexPai(index));
      index = this.indexPai(index);
    }
  }

  extractMin() {
    if (this.filaP.length === 0) {
      throw new Error("Lista vazia");
    }
    if (this.filaP.length === 1) {
      return this.filaP.pop();
    }

    const min = this.filaP[0];
    this.filaP[0] = this.filaP.pop();
    this.empilharAbaixo();

    return min;
  }

  empilharAbaixo() {
    let index = 0;

    while (this.indexFilhoEsquerda(index) < this.filaP.length) {
      let menorIndex = this.indexFilhoEsquerda(index);

      if (
        this.indexFilhoDireita(index) < this.filaP.length &&
        this.filaP[this.indexFilhoDireita(index)].priority <
          this.filaP[menorIndex].priority
      ) {
        menorIndex = this.indexFilhoDireita(index);
      }
      if (this.filaP[index].priority <= this.filaP[menorIndex].priority) {
        break;
      }
      this.trocar(index, menorIndex);
      index = menorIndex;
    }
  }

  estaVazio() {
    return this.filaP.length === 0;
  }
}

function AlgoritmoDeDijkstra(grafo, vertice_inicial) {
  const distancia_arestas = {};
  const caminho = {};
  const lista = new ListaDePrioridade();

  for (let vertice in grafo) {
    if (vertice === vertice_inicial) {
      distancia_arestas[vertice] = 0;
      lista.inserir({ value: vertice, priority: 0 });
    } else {
      distancia_arestas[vertice] = Infinity;
      lista.inserir({ value: vertice, priority: Infinity });
    }
    caminho[vertice] = null;
  }

  // console.log(distancia_arestas); -> {A: 0, B: Infinity, C: Infinity, D: Infinity}
  // console.log(caminho); -> {A: null, B: null, C: null, D: null}
  
  while (!lista.estaVazio()) {
    const { value: vertice_atual } = lista.extractMin(); // retorna o indice do array, 'A'

    for (let vertice_ligacao in grafo[vertice_atual]) { // pega cada valor do array 'A' por vez
      let distancia = grafo[vertice_atual][vertice_ligacao];
      let nova_distancia = distancia_arestas[vertice_atual] + distancia; // infinity + num = num

      if (nova_distancia < distancia_arestas[vertice_ligacao]) { //infinity é sempre maior
        distancia_arestas[vertice_ligacao] = nova_distancia;
        caminho[vertice_ligacao] = vertice_atual;
        lista.inserir({ value: vertice_ligacao, priority: nova_distancia });
      }
    }
  }
  

  return { distancia_arestas, caminho };
}

// criando grafo
const grafo = {
  A: { B: 1, C: 4 },
  B: { A: 1, C: 2, D: 5 },
  C: { A: 4, B: 2, D: 1 },
  D: { B: 5, C: 1 },
};

const { distancia_arestas, caminho } = AlgoritmoDeDijkstra(grafo, "A");
console.log(`Distância das arestas do menor caminho:`);
console.log(distancia_arestas);

console.log(`Menor caminho:`);
console.log(caminho);

let testeArr = [1,2,3];
testeArr.pop();
console.log(testeArr);
console.log(Infinity + 1);