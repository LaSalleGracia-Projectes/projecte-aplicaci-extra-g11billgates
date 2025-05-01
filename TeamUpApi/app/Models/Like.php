<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Like extends Model
{
    use HasFactory;

    protected $table = 'likes'; 

    protected $fillable = [
        'IDUsuario1',
        'IDUsuario2',
        'Fecha',
    ];

    public $timestamps = false; 


    public function usuarioQueDaLike()
    {
        return $this->belongsTo(User::class, 'IDUsuario1');
    }

    public function usuarioQueRecibeLike()
    {
        return $this->belongsTo(User::class, 'IDUsuario2');
    }
}
