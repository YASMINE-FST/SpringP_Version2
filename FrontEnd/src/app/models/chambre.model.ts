export interface Chambre {
  idChambre: number;
  nombreChambre: number;
  typeC: 'SIMPLE' | 'DOUBLE' | 'TRIPLE';
  idBloc?: number;
  nomBloc?: string;
  idFoyer?: number;
  nomFoyer?: string;
}
